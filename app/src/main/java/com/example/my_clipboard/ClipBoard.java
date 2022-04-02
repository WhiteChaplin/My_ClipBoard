package com.example.my_clipboard;

public class ClipBoard {
    public String clipBoardName;
    public String clipBoard;

    public ClipBoard()
    {

    }

    public ClipBoard(String clipBoardName, String clipBoard){
        this.clipBoardName = clipBoardName;
        this.clipBoard = clipBoard;
    }

    public String getClipBoard() {
        return clipBoard;
    }

    public void setClipBoard(String clipBoard) {
        this.clipBoard = clipBoard;
    }

    public String getClipBoardName() {
        return clipBoardName;
    }

    public void setClipBoardName(String clipBoardName) {
        this.clipBoardName = clipBoardName;
    }
}
