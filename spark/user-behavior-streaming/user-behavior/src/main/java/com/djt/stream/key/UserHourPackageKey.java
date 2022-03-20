package com.djt.stream.key;

import scala.math.Ordered;
import java.io.Serializable;

public class UserHourPackageKey implements Ordered<UserHourPackageKey>, Serializable
{
  public Long userId;
  public String hour;
  public String packageName;

  @Override
  public int compareTo(UserHourPackageKey that)
  {
    if (this.userId == that.getUserId()) {
      if (this.hour.compareTo(that.getHour())==0) {
        return this.packageName.compareTo(that.getPackageName());
      } else {
        return this.hour.compareTo(that.getHour());
      }
    } else {
      Long n = this.userId - that.getUserId();
      return n > 0 ? 1 : (n == 0 ? 0 : -1);
    }
  }

  @Override
  public int compare(UserHourPackageKey that)
  {
    return this.compareTo(that);
  }

  @Override
  public boolean $greater(UserHourPackageKey that)
  {
    if (this.compareTo(that) > 0) {
      return true;
    }

    return false;
  }

  @Override
  public boolean $less(UserHourPackageKey that)
  {
    if (this.compareTo(that) < 0) {
      return true;
    }

    return false;
  }

  @Override
  public boolean $less$eq(UserHourPackageKey that)
  {
    if (this.compareTo(that) <= 0) {
      return true;
    }

    return false;
  }

  @Override
  public boolean $greater$eq(UserHourPackageKey that) {
    if (this.compareTo(that) >= 0) {
      return true;
    }

    return false;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserHourPackageKey that = (UserHourPackageKey) o;

    if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
    if (hour != null ? !hour.equals(that.hour) : that.hour != null) return false;
    return !(packageName != null ? !packageName.equals(that.packageName) : that.packageName != null);

  }

  @Override
  public int hashCode() {
    int result = userId != null ? userId.hashCode() : 0;
    result = 31 * result + (hour != null ? hour.hashCode() : 0);
    result = 31 * result + (packageName != null ? packageName.hashCode() : 0);
    return result;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public String getHour() {
    return hour;
  }

  public void setHour(String hour) {
    this.hour = hour;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public String toString() {
    return "UserHourPackageKey{" +
            "userId=" + userId +
            ", hour='" + hour + '\'' +
            ", packageName='" + packageName + '\'' +
            '}';
  }
}
