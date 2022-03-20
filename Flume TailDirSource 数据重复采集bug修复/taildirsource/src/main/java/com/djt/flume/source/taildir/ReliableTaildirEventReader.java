/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.djt.flume.source.taildir;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.gson.stream.JsonReader;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.annotations.InterfaceAudience;
import org.apache.flume.annotations.InterfaceStability;
import org.apache.flume.client.avro.ReliableEventReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@InterfaceAudience.Private
@InterfaceStability.Evolving
public class ReliableTaildirEventReader implements ReliableEventReader {
  private static final Logger logger = LoggerFactory.getLogger(ReliableTaildirEventReader.class);

  private final List<TaildirMatcher> taildirCache;
  private final Table<String, String, String> headerTable;

  private TailFile currentFile = null;
  //保存inode（文件唯一标识）与TailFile对应关系
  private Map<Long, TailFile> tailFiles = Maps.newHashMap();
  private long updateTime;
  private boolean addByteOffset;
  private boolean cachePatternMatching;
  private boolean committed = true;
  private final boolean annotateFileName;
  private final String fileNameHeader;

  /**
   * Create a ReliableTaildirEventReader to watch the given directory.
   */
  private ReliableTaildirEventReader(Map<String, String> filePaths,
      Table<String, String, String> headerTable, String positionFilePath,
      boolean skipToEnd, boolean addByteOffset, boolean cachePatternMatching,
      boolean annotateFileName, String fileNameHeader) throws IOException {
    // Sanity checks
    Preconditions.checkNotNull(filePaths);
    Preconditions.checkNotNull(positionFilePath);

    if (logger.isDebugEnabled()) {
      logger.debug("Initializing {} with directory={}, metaDir={}",
          new Object[] { ReliableTaildirEventReader.class.getSimpleName(), filePaths });
    }

    List<TaildirMatcher> taildirCache = Lists.newArrayList();
    //循环读取filePaths，添加taildirCache
    for (Entry<String, String> e : filePaths.entrySet()) {
      taildirCache.add(new TaildirMatcher(e.getKey(), e.getValue(), cachePatternMatching));
    }
    logger.info("taildirCache: " + taildirCache.toString());
    logger.info("headerTable: " + headerTable.toString());

    this.taildirCache = taildirCache;
    this.headerTable = headerTable;
    this.addByteOffset = addByteOffset;
    this.cachePatternMatching = cachePatternMatching;
    this.annotateFileName = annotateFileName;
    this.fileNameHeader = fileNameHeader;

    //新增文件添加到tailFiles集合
    updateTailFiles(skipToEnd);
    // positionFilePath:/home/hadoop/data/flume/taildir_position.json
    logger.info("Updating position from position file: " + positionFilePath);
    //根据position文件更新tailFiles集合
    loadPositionFile(positionFilePath);
  }

  /**
   * Load a position file which has the last read position of each file.
   * If the position file exists, update tailFiles mapping.
   */
  public void loadPositionFile(String filePath) {
    Long inode, pos;
    String path;
    FileReader fr = null;
    JsonReader jr = null;
    try {
      fr = new FileReader(filePath);
      jr = new JsonReader(fr);
      jr.beginArray();
      while (jr.hasNext()) {
        inode = null;
        pos = null;
        path = null;
        jr.beginObject();
        while (jr.hasNext()) {
          switch (jr.nextName()) {
            case "inode":
              inode = jr.nextLong();
              break;
            case "pos":
              pos = jr.nextLong();
              break;
            case "file":
              path = jr.nextString();
              break;
          }
        }
        jr.endObject();

        for (Object v : Arrays.asList(inode, pos, path)) {
          Preconditions.checkNotNull(v, "Detected missing value in position file. "
              + "inode: " + inode + ", pos: " + pos + ", path: " + path);
        }
        TailFile tf = tailFiles.get(inode);
        //更新inode与TailFile映射关系
        // if (tf != null && tf.updatePos(path, inode, pos)) {
        //参数path改为tf.getPath()，保证this.path.equals(path)相等，执行内部updatePos方法更新采集位置。
        // 因为如果出现文件名字修改，old文件path肯定不等于new文件path
        //从taildir_position.json文件中得到Path和inode映射信息不能用来判断它们之间的映射关系是否改变
        // ，只能用新获取的TailFile中的Path和inode来判断是否改动
        if (tf != null && tf.updatePos(tf.getPath(), inode, pos)) {
          //更新inode与tf映射
          tailFiles.put(inode, tf);
        } else {
          logger.info("Missing file: " + path + ", inode: " + inode + ", pos: " + pos);
        }

      }
      jr.endArray();
    } catch (FileNotFoundException e) {
      logger.info("File not found: " + filePath + ", not updating position");
    } catch (IOException e) {
      logger.error("Failed loading positionFile: " + filePath, e);
    } finally {
      try {
        if (fr != null) fr.close();
        if (jr != null) jr.close();
      } catch (IOException e) {
        logger.error("Error: " + e.getMessage(), e);
      }
    }
  }

  public Map<Long, TailFile> getTailFiles() {
    return tailFiles;
  }

  public void setCurrentFile(TailFile currentFile) {
    this.currentFile = currentFile;
  }

  @Override
  public Event readEvent() throws IOException {
    List<Event> events = readEvents(1);
    if (events.isEmpty()) {
      return null;
    }
    return events.get(0);
  }

  @Override
  public List<Event> readEvents(int numEvents) throws IOException {
    return readEvents(numEvents, false);
  }

  @VisibleForTesting
  public List<Event> readEvents(TailFile tf, int numEvents) throws IOException {
    setCurrentFile(tf);
    return readEvents(numEvents, true);
  }

  public List<Event> readEvents(int numEvents, boolean backoffWithoutNL)
      throws IOException {
    if (!committed) {
      if (currentFile == null) {
        throw new IllegalStateException("current file does not exist. " + currentFile.getPath());
      }
      logger.info("Last read was never committed - resetting position");
      long lastPos = currentFile.getPos();
      currentFile.updateFilePos(lastPos);
    }
    List<Event> events = currentFile.readEvents(numEvents, backoffWithoutNL, addByteOffset);
    if (events.isEmpty()) {
      return events;
    }

    Map<String, String> headers = currentFile.getHeaders();
    if (annotateFileName || (headers != null && !headers.isEmpty())) {
      for (Event event : events) {
        if (headers != null && !headers.isEmpty()) {
          event.getHeaders().putAll(headers);
        }
        if (annotateFileName) {
          event.getHeaders().put(fileNameHeader, currentFile.getPath());
        }
      }
    }
    committed = false;
    return events;
  }

  @Override
  public void close() throws IOException {
    for (TailFile tf : tailFiles.values()) {
      if (tf.getRaf() != null) tf.getRaf().close();
    }
  }

  /** Commit the last lines which were read. */
  @Override
  public void commit() throws IOException {
    if (!committed && currentFile != null) {
      long pos = currentFile.getLineReadPos();
      currentFile.setPos(pos);
      currentFile.setLastUpdated(updateTime);
      committed = true;
    }
  }

  /**
   * Update tailFiles mapping if a new file is created or appends are detected
   * to the existing file.
   */
  public List<Long> updateTailFiles(boolean skipToEnd) throws IOException {
    updateTime = System.currentTimeMillis();
    List<Long> updatedInodes = Lists.newArrayList();
  /*获取缓存中的taildir；taildirCache的数据类型是List<TaildirMatcher>--->找到TaildirMatcher构造器，有如下形参
     (String fileGroup, String dirPath, String filePath,boolean cachePatternMatching)
     taildir:{filegroup='f1', filePattern='/logger/flume/xxx/escmbcxxx.log', cached=true}
     */
    //循环监控目录文件
    for (TaildirMatcher taildir : taildirCache) {
       /*taildir.getFileGroup()返回的是filegroups；headerTable的数据类型是Table<String, String, String>；Table
      类找不到...,所以暂时看不到row方法，不过可以猜到：headers头部信息存放的是组和文件的绝对路径的map集合*/
       /*
       *  headerTable.row 返回指定rowKey下的所有columnKey与value映射. 姓:名:value
       * */
      Map<String, String> headers = headerTable.row(taildir.getFileGroup());
      //taildir.getMatchingFiles()返回的是按最后修改时间倒序的file集合
      for (File f : taildir.getMatchingFiles()) {
        //获取文件对应的inode值
        long inode = getInode(f);
        //tailFiles根据inode在获取文件对象
        TailFile tf = tailFiles.get(inode);

        //如果tf不存在，或者tf的路径跟f的路径不一致
        //如果文件改了，tf和f 的inode一致，但路径不一致
        //if (tf == null || !tf.getPath().equals(f.getAbsolutePath())) {
          if (tf == null) {
          //skipToEnd默认为false，故此时从0消费
          long startPos = skipToEnd ? f.length() : 0; //设置开始的pos值为0
          //根据f具体文件生成TailFile
          tf = openFile(f, headers, inode, startPos);//创建一个包含该文件信息的TailFile对象
        } else {
            //TailFile是空值的话，里面也取不到路径，更不可能和file集合中的路径相同；所以进去else的情况只有一个
            //TailFile有值，后面不再判断
            //两个文件更新时间不一致，或者消费位置跟文件长度不一致，此时需要更新tf
            // 对比TailFile中文件的最后更新时间是否小于扫描上来的file的mtime(小就证明文件有更新)||
            //pos记录的字节数不等于该文件的实际大小--->这个作用防止仅仅往后修改了文件的mtime
          boolean updated = tf.getLastUpdated() < f.lastModified() || tf.getPos() != f.length();
          if (updated) {
            if (tf.getRaf() == null) {
              //返回的raf --->通过RandomAccessFile类下的seek()方法，按字节获取文件的偏移位置
              // 并且从这之后开始读取新的内容；如果这个值为空，那么无法断点续读，只能从新开始
              tf = openFile(f, headers, inode, tf.getPos());
            }
            /*正常情况下一定是新扫描过得文件一定大于TailFile中缓存的文件大小；这里做了一个异常判断，就是如果相反，新扫描的文件
            反而比原来缓存中记录的文件字节小了，那么肯定发生了什么错误，直接重置pos*/
            if (f.length() < tf.getPos()) {
              logger.info("Pos " + tf.getPos() + " is larger than file size! "
                  + "Restarting from pos 0, file: " + tf.getPath() + ", inode: " + inode);
              tf.updatePos(tf.getPath(), inode, 0);
            }
          }

          //***************modify  begin*****************
          //如果同一个文件的文件名称修改后，重新生成tf
          if (!tf.getPath().equals(f.getAbsolutePath())) {
            tf = openFile(f, headers, inode, tf.getPos());
          }
          //**************modify  end*********************
            /*为真。这段代码表示这个文件标记为需要tail；为假则不需要*/
          tf.setNeedTail(updated);
        }

        tailFiles.put(inode, tf);//注意作用域
        updatedInodes.add(inode);//把inode添加到updatedInodes
      }
    }
    return updatedInodes;
  }

  public List<Long> updateTailFiles() throws IOException {
    return updateTailFiles(false);
  }


  private long getInode(File file) throws IOException {
    long inode = (long) Files.getAttribute(file.toPath(), "unix:ino");
    return inode;
  }

  private TailFile openFile(File file, Map<String, String> headers, long inode, long pos) {
    try {
      logger.info("Opening file: " + file + ", inode: " + inode + ", pos: " + pos);
      return new TailFile(file, headers, inode, pos);
    } catch (IOException e) {
      throw new FlumeException("Failed opening file: " + file, e);
    }
  }

  /**
   * Special builder class for ReliableTaildirEventReader
   */
  public static class Builder {
    private Map<String, String> filePaths;
    private Table<String, String, String> headerTable;
    private String positionFilePath;
    private boolean skipToEnd;
    private boolean addByteOffset;
    private boolean cachePatternMatching;
    private Boolean annotateFileName =
            TaildirSourceConfigurationConstants.DEFAULT_FILE_HEADER;
    private String fileNameHeader =
            TaildirSourceConfigurationConstants.DEFAULT_FILENAME_HEADER_KEY;

    public Builder filePaths(Map<String, String> filePaths) {
      this.filePaths = filePaths;
      return this;
    }

    public Builder headerTable(Table<String, String, String> headerTable) {
      this.headerTable = headerTable;
      return this;
    }

    public Builder positionFilePath(String positionFilePath) {
      this.positionFilePath = positionFilePath;
      return this;
    }

    public Builder skipToEnd(boolean skipToEnd) {
      this.skipToEnd = skipToEnd;
      return this;
    }

    public Builder addByteOffset(boolean addByteOffset) {
      this.addByteOffset = addByteOffset;
      return this;
    }

    public Builder cachePatternMatching(boolean cachePatternMatching) {
      this.cachePatternMatching = cachePatternMatching;
      return this;
    }

    public Builder annotateFileName(boolean annotateFileName) {
      this.annotateFileName = annotateFileName;
      return this;
    }

    public Builder fileNameHeader(String fileNameHeader) {
      this.fileNameHeader = fileNameHeader;
      return this;
    }

    public ReliableTaildirEventReader build() throws IOException {
      return new ReliableTaildirEventReader(filePaths, headerTable, positionFilePath, skipToEnd,
                                            addByteOffset, cachePatternMatching,
                                            annotateFileName, fileNameHeader);
    }
  }

}
