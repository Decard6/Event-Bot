package model;

import java.util.List;

public interface Fetchable<T> {
    List<T> fetch();
}
