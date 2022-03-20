package com.djt.dao;

import org.apache.hadoop.fs.Path;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public abstract class AbstractTVDAO {

	abstract public Map<String, String> parseTVObj(Path[] cacheFilesPaths) throws IOException;

}
