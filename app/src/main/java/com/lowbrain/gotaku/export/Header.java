package com.lowbrain.gotaku.export;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Header {

    /** 文字コード */
    private static final String CHARSET_5TQ = "Windows-31J";

    /** ジャンル */
    private String genre = null;

    /** 未使用 */
    private short pass = 0;

    /** ジャンル中の問題数（ブロック数と等価） */
    private short size = 0;

    /** アクセスのために読み飛ばすブロック数 */
    private short skip = 0;

    /** プレーヤーデータファイル名 */
    private String dataFile = null;

    /** マジックコード（5TAKUQDT or 5TAKUQDX） */
    private String magicCd = null;

    /** ブロックアライメントの調整用詰めもの */
    private String blockAlignment = null;

    /**
     * 
     */
    private Header() {
    }

    /**
     * 5TQファイルの1ヘッダブロックよりヘッダデータを生成します。
     * 
     * @param block 1ヘッダブロック(256Byte)
     * @return 5TQファイルヘッダ定義
     */
    public static Header genHeader(byte[] block) {
        Header header = new Header();

        try {

            header.genre = (new String(block, 0, 16, CHARSET_5TQ)).trim();

            ByteBuffer buffer = ByteBuffer.wrap(block, 16, 6);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            header.pass = buffer.getShort();
            header.size = buffer.getShort();
            header.skip = buffer.getShort();
            
            header.dataFile = new String(block, 22, 12, CHARSET_5TQ);
            
            header.magicCd = new String(block, 34, 8, CHARSET_5TQ);
            
            header.blockAlignment = new String(block, 42, 214, CHARSET_5TQ);

        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return header;
    }

    /**
     * 
     * @param index
     * @return
     */
    public boolean isBlockRange(int index) {
        short blockIndex = (short)(index / 256);
        return (skip <= blockIndex && blockIndex < (skip + size)) ? true : false;
    }

    /**
     * 
     * @return
     */
    public String getGenre() {
        return genre;
    }

    /**
     * 
     * @return
     */
    public short getPass() {
        return pass;
    }

    /**
     * 
     * @return
     */
    public short getSize() {
        return size;
    }

    /**
     * 
     * @return
     */
    public short getSkip() {
        return skip;
    }

    /**
     * 
     * @return
     */
    public String getDataFile() {
        return dataFile;
    }

    /**
     * 
     * @return
     */
    public String getMagicCd() {
        return magicCd;
    }

    /**
     * 
     * @return
     */
    public String getBlockAlignment() {
        return blockAlignment;
    }

    @Override
    public String toString() {
        return "Header [" + "genre=" + genre + ", " + "size=" + size + ", " + "skip=" + skip + ", " + "dataFile="
                + dataFile + ", " + "magicCd=" + magicCd + "]";

        // return "Header [" + "genre=" + genre + ", " + "pass=" + pass + ", " + "size=" + size + ", " + "skip=" + skip + ", " + "dataFile="
        //         + dataFile + ", " + "magicCd=" + magicCd + ", " + "blockAlignment=" + blockAlignment + "]";
    }

}
