package com.lowbrain.gotaku.export;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Question {
    /** 文字コード */
    private static final String CHARSET_5TQ = "Shift-JIS";

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
        for (int i = 0; i < block.length; i = i + 2) {
            if (!(block[i] == 0x20 && block[i + 1] == 0x20)) {
                decodeBlock[i] = (byte) (block[i] ^ 0x80);
                decodeBlock[i + 1] = (byte) (block[i + 1] ^ 0x80);
            } else {
                decodeBlock[i] = block[i];
                decodeBlock[i + 1] = block[i + 1];
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
