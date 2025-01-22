package net.karoll.cigarettemod.handlers;

public class ClientDataHandler {
    private static int slotsUsed = -1;
    private static String UIDProcessing = "";

    public static void setSlotsUsed(int data) {
        slotsUsed = data;
    }

    public static int getSlotsUsed() {
        return slotsUsed;
    }
    public static void setUIDProcessing(String data) {
        UIDProcessing = data;
    }
    public static String getUIDProcessing() {
        return UIDProcessing;
    }
}
