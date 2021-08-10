package com.lowbrain.gotaku.export;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Question {
    /** 文字コード */
    private static final String CHARSET_5TQ = "Windows-31J";

    /** ジャンル */
    private String genre = null;

    /** 問題 */
    private String question = null;

    /** 回答 */
    private List<String> answerList = null;

    private Question() {
    }

    public static Question genQuestion(byte[] block, String genre) {
        // BLOCKをエンコード（0x80 で xor）
        byte[] decodeBlock = new byte[block.length];
        int cache = 0; 
        for (int i = 0; i < block.length; i++) {
            if ((0x81 <= cache && cache <= 0x9f) || (0xe0 <= cache && cache <= 0xfc)) {
                // 前のバイトが2バイト文字の第1バイトと一致する場合は無条件にエンコード
                decodeBlock[i] = (byte)(block[i] ^ 0x80);
                cache = 0;
            } else if (block[i] != 0x20) {
                // 0x20(スペース)ではない場合も無条件にエンコード
                decodeBlock[i] = (byte)(block[i] ^ 0x80);
                cache = block[i] ^ 0x80;
            } else {
                // 0x20(スペース)の場合のみエンコードしない
                decodeBlock[i] = 0x20;
                cache =0;
            }
        }

        Question q = new Question();
        q.genre = genre;
        q.answerList = new ArrayList<String>();
        try {
            q.question = (new String(decodeBlock, 0, 116, CHARSET_5TQ)).trim();
            for (int i = 0; i < 5; i++) {
                q.answerList.add((new String(decodeBlock, 116 + 28 * i, 28, CHARSET_5TQ)).trim());
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return q;
    }

    public String getGenre() {
        return genre;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public String getAnswer() {
        return answerList.get(0);
    }

    @Override
    public String toString() {
        return "Question [" + "genre=" + genre + ", question=" + question + ", answerList=" + answerList + "]";
    }

    public String toCsvString() {
        return "\"" + genre + "\", " + "\"" + question + "\"," + "\"" + answerList.get(0) + "\"," + "\""
                + answerList.get(1) + "\"," + "\"" + answerList.get(2) + "\"," + "\"" + answerList.get(3) + "\"," + "\""
                + answerList.get(4) + "\"";
    }

}
