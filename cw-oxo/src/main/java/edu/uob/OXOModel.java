package edu.uob;
import java.util.*;

class OXOModel {
  private ArrayList<ArrayList<OXOPlayer>> cells;
  private ArrayList<OXOPlayer> players;
  private int currentPlayerNumber;
  private OXOPlayer winner;
  private boolean gameDrawn;
  private int winThreshold;


  public OXOModel(int numberOfRows, int numberOfColumns, int winThresh) {
    winThreshold = winThresh;
    cells = new ArrayList<ArrayList<OXOPlayer>>();
    for (int a=0; a<numberOfRows; a++){
      ArrayList<OXOPlayer> newRow = new ArrayList<OXOPlayer>(numberOfColumns);
      for (int b=0; b<numberOfColumns; b++){
        newRow.add(null);
      }
      cells.add(newRow);
    }
    players = new ArrayList<OXOPlayer>();
  }


  public ArrayList<ArrayList<OXOPlayer>> getCell() {
    return cells;
  }


  public ArrayList<OXOPlayer> getRow(int rowNumber) {
    return cells.get(rowNumber);
  }


  public int getNumberOfPlayers() {
    // return players.length;
    return players.size();
  }


  public void addPlayer(OXOPlayer player) {
    players.add(player);
  }


  public OXOPlayer getPlayerByNumber(int number) {
    return players.get(number);
    // return players[number];
  }


  public OXOPlayer getWinner() {
    return winner;
  }


  public void setWinner(OXOPlayer player) {
    winner = player;
  }


  public int getCurrentPlayerNumber() {
    return currentPlayerNumber;
  }


  public void setCurrentPlayerNumber(int playerNumber) {
    currentPlayerNumber = playerNumber;
  }


  public int getNumberOfRows() {
    return cells.size();
    // return cells.length;
  }


  public int getNumberOfColumns() {
    return cells.get(0).size();
    // return cells[0].length;
  }


  public OXOPlayer getCellOwner(int rowNumber, int colNumber) {
    return cells.get(rowNumber).get(colNumber);
    // return cells[rowNumber][colNumber];
  }


  public void setCellOwner(int rowNumber, int colNumber, OXOPlayer player) {
    cells.get(rowNumber).set(colNumber, player);
    // cells[rowNumber][colNumber] = player;
  }


  public void setWinThreshold(int winThresh) {
    winThreshold = winThresh;
  }


  public int getWinThreshold() {
    return winThreshold;
  }


  public void setGameDrawn() {
    gameDrawn = true;
  }


  public boolean isGameDrawn() {
    return gameDrawn;
  }

}
