package codec.javaO;

import java.io.Serializable;
import java.nio.ByteBuffer;

public class UserInfo implements Serializable {

    /**
     * 默认的序列号
     */
    private static final long serialVersionUID = 1L;

    private String userName;

    private int userID;

    public UserInfo buildUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public UserInfo buildUserID(int userID) {
        this.userID = userID;
        return this;
    }

    /**
     * @return the userName
     */
    public final String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public final void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userID
     */
    public final int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public final void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * 基于ByteBuffer的通用二进制编解码技术对UserInfo对象进行编码
     *
     * @return
     */
    public byte[] codeC(ByteBuffer byteBuffer) {
        //ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byte[] name = this.userName.getBytes();
        byteBuffer.putInt(name.length);
        byteBuffer.put(name);
        byteBuffer.putInt(this.userID);
        byteBuffer.flip();
        name = null;
        byte[] result = new byte[byteBuffer.remaining()];
        return result;
    }
}