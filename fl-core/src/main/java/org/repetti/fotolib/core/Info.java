package org.repetti.fotolib.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.repetti.utils.JsonHelper;

/**
 * Date: 24/07/15
 *
 * @author repetti
 */
public class Info {
    private final String path;
    private final String name;
    private final String hash; //sha512
    private final long lastModified;
    private final long size;

    public static final String HASH = "hash";
    public static final String NAME = "name";
    public static final String PATH = "path";
    public static final String SIZE = "size";
    public static final String LAST_MODIFIED = "lastModified";

    public Info(String path, String name, String hash, long lastModified, long size) {
        this.path = path;
        this.name = name;
        this.hash = hash;
        this.lastModified = lastModified;
        this.size = size;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getHash() {
        return hash;
    }

    public long getLastModified() {
        return lastModified;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Info info = (Info) o;

//        if (lastModified != info.lastModified) return false;
        return hash.equals(info.hash);

    }

    @Override
    public int hashCode() {
        int result = hash.hashCode();
        result = 31 * result + (int) (lastModified ^ (lastModified >>> 32));
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Info{");
        sb.append("path='").append(path).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", hash='").append(hash).append('\'');
        sb.append(", lastModified=").append(lastModified);
        sb.append(", size=").append(size);
        sb.append('}');
        return sb.toString();
    }

    public ObjectNode toJson() {
        ObjectNode ret = JsonHelper.newObjectNode();
        ret.put(HASH, hash);
        ret.put(LAST_MODIFIED, lastModified);
        ret.put(SIZE, size);
        ret.put(PATH, path);
        ret.put(NAME, name);
        return ret;
    }

    public static Info fromJson(JsonNode j) {
        if (!j.isObject()) {
            throw new IllegalArgumentException("not an object" + j);
        }
        String path = j.get(PATH).asText();
        String name = j.get(NAME).asText();
        String hash = j.get(HASH).asText();
        long size = j.get(SIZE).asLong();
        long lastModified = j.get(LAST_MODIFIED).asLong();
        return new Info(path, name, hash, lastModified, size);
    }
}
