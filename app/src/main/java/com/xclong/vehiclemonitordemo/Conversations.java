package com.xclong.vehiclemonitordemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by xcl02 on 2016/7/27.
 */
public class Conversations {
    private static final String[] MESSAGES = new String[]{
            "电源故障",
            "两通阀故障",
            "计量泵故障",
            "喷嘴堵故障",
            "LQ84-86故障",
            "NOx超标故障",
            "NOx传感器故障",
            "尿素液位故障",
            "尿素质量故障",
            "SCR故障",
            "DePm催化剂故障"
    };

    public Conversations() {
    }

    public static class Conversion {
        private final int conversation_id;
        private final String title;
        private final List<String> messages;
        private final long timestamp;

        Conversion(int conversation_id, String title, List<String> messages) {
            this.conversation_id = conversation_id;
            this.title = title;
            this.messages = messages;
            this.timestamp = System.currentTimeMillis();
        }

        public int getConversation_id() {
            return conversation_id;
        }

        public List<String> getMessages() {
            return messages;
        }

        public String getMessagesString() {
            return getMessages().toString();
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "[Conversions : conversation_id = " + conversation_id + " , "
                    + "messages = " + messages + " , "
                    + "timestamp = " + timestamp + " ]";
        }
    }

    /*public static Conversion getUnreadConversations(int howMangMessagesPerConversion) {
        Conversion conversion = new Conversion(ThreadLocalRandom.current().nextInt(), makeMessages(howMangMessagesPerConversion));
        return conversion;
    }*/

    public static Conversion getUnreadConversation(String plate_number, int[] m) {
        Conversion conversion = new Conversion(ThreadLocalRandom.current().nextInt(), plate_number, makeMessages(m));
        return conversion;
    }

    private static List<String> makeMessages(int howMangMessagesPerConversion) {
        int maxLen = MESSAGES.length;
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < howMangMessagesPerConversion; i++) {
            messages.add(MESSAGES[ThreadLocalRandom.current().nextInt(0, maxLen)]);
        }
        return messages;
    }

    private static List<String> makeMessages(int[] m) {
//        int maxLen = MESSAGES.length;
//        int count = ThreadLocalRandom.current().nextInt(1, maxLen);
        List<String> messages = new ArrayList<>();
        for (int i = 0; i < m.length; i++) {
            if (m[i] == 1) {
                messages.add(MESSAGES[i]);
            }
        }
        return messages;
    }
}
