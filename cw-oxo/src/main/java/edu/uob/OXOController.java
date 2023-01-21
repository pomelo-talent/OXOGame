package edu.uob;
import edu.uob.OXOMoveException.*;
import java.util.ArrayList;

class OXOController {
  OXOModel gameModel;

  private int NumberOfPlayers;
  private int currPlayerIndex;
  private int allSteps;


  public OXOController(OXOModel model) {
    gameModel = model;

    NumberOfPlayers = gameModel.getNumberOfPlayers();
    currPlayerIndex = 0;
    //OXOPlayer firstPlayer = gameModel.getPlayerByNumber(currPlayerIndex);
    gameModel.setCurrentPlayerNumber(currPlayerIndex);
    allSteps = 0;

  }


  public void handleIncomingCommand(String command) throws OXOMoveException {
    if (gameModel.getWinner()!=null || gameModel.isGameDrawn()) {
      return;
    }
    if (command.length()!=2) {
      throw new InvalidIdentifierLengthException(command.length());
    }

    command = command.toLowerCase();
    int tempRowNumber = command.charAt(0)-'a';
    int tempColNumber = command.charAt(1)-'1';
    if (!isCharacter(command.charAt(0))) {
      throw new InvalidIdentifierCharacterException(RowOrColumn.ROW,command.charAt(0));
    }
    if (!isDigit(command.charAt(1))) {
      throw new InvalidIdentifierCharacterException(RowOrColumn.COLUMN,command.charAt(1));
    }
    if (tempColNumber<0 || tempRowNumber>=gameModel.getNumberOfRows()) {
      throw new OutsideCellRangeException(RowOrColumn.ROW, tempRowNumber);
    }
    if ( tempColNumber<0 || tempColNumber>=gameModel.getNumberOfColumns()) {
      throw new OutsideCellRangeException(RowOrColumn.COLUMN, tempColNumber);
    }
    if (gameModel.getCellOwner(tempRowNumber, tempColNumber)!=null) {
      throw new CellAlreadyTakenException(tempRowNumber, tempColNumber);
    }
    else {
      gameModel.setCellOwner(tempRowNumber, tempColNumber, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));

      // check whether the game end in a draw
      allSteps++;
      if (allSteps == gameModel.getNumberOfRows() * gameModel.getNumberOfColumns()) {
        gameModel.setGameDrawn();
      }

      // check whether one of the players wins the game
      if (isWin(tempRowNumber, tempColNumber, gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()))) {
        gameModel.setWinner(gameModel.getPlayerByNumber(gameModel.getCurrentPlayerNumber()));
      }

      switchPlayer();
    }
  }


  public boolean isCharacter(char commandContent) {
    if (commandContent-'a'>=0 && commandContent-'a'<26) {
      return true;
    }
    else {
      return false;
    }
  }


  public boolean isDigit(char commandContent) {
    if (commandContent-'0'>=0 && commandContent-'0'<=9) {
      return true;
    }
    else {
      return false;
    }
  }


  public void switchPlayer() {
    currPlayerIndex++;
    if (currPlayerIndex == NumberOfPlayers) {
      // the next turn is Player 0
      currPlayerIndex = 0;
    }
    // else, the next turn is Player 1
    //OXOPlayer nextPlayer = gameModel.getPlayerByNumber(currPlayerIndex);
    gameModel.setCurrentPlayerNumber(currPlayerIndex);
  }


  public boolean isWin(int rowNum, int colNum, OXOPlayer currPlayer) {
    int rowSum = 0;
    int colSum = 0;
    int diaLSum = 0;
    int diaRSum = 0;

    // Wins vertically
    for (int i=0; i<gameModel.getNumberOfRows(); i++) {
      if (gameModel.getCellOwner(i, colNum)==currPlayer) {
        colSum++;
      }
    }

    // Wins horizontally
    for (int j=0; j<gameModel.getNumberOfColumns(); j++) {
      if (gameModel.getCellOwner(rowNum, j)==currPlayer) {
        rowSum++;
      }
    }

    // Wins left-diagonally
    for (int z=0; z<gameModel.getWinThreshold(); z++) {
      if (gameModel.getCellOwner(z, z)==currPlayer) {
        diaLSum++;
      }
    }

    // Wins right-diagonally
    for (int k=0; k<gameModel.getWinThreshold(); k++) {
      if (gameModel.getCellOwner(k, gameModel.getWinThreshold()-k-1)==currPlayer) {
        diaRSum++;
      }
    }

    if (colSum==gameModel.getWinThreshold() ||
            rowSum==gameModel.getWinThreshold() ||
            diaLSum==gameModel.getWinThreshold() ||
            diaRSum==gameModel.getWinThreshold()) {
      return true;
    } else {
      return false;
    }

  }


  public void addRow() {
    ArrayList<OXOPlayer> addedRow = new ArrayList<OXOPlayer>(gameModel.getNumberOfColumns());
    for (int q=0; q< gameModel.getNumberOfColumns(); q++) {
      addedRow.add(null);
    }
    gameModel.getCell().add(addedRow);
  }


  public void removeRow() {
    gameModel.getCell().remove(gameModel.getNumberOfRows()-1);
  }


  public void addColumn() {
    for (int x=0; x<gameModel.getNumberOfRows(); x++) {
      gameModel.getRow(x).add(null);
    }
  }


  public void removeColumn() {
    for (int x=0; x<gameModel.getNumberOfRows(); x++) {
      gameModel.getRow(x).remove(gameModel.getNumberOfColumns()-1);
    }
  }


  public void increaseWinThreshold() {
    int tempThresh = gameModel.getWinThreshold();
    tempThresh++;
    gameModel.setWinThreshold(tempThresh);
  }


  public void decreaseWinThreshold() {
    int tempThresh = gameModel.getWinThreshold();
    tempThresh--;
    gameModel.setWinThreshold(tempThresh);
  }


  // add additional players
  public void addAddPlayer(char symbol) {
    // we need to use alternative player characters for the additional players
    for (int y = 0; y < gameModel.getCurrentPlayerNumber(); y++) {
      if (symbol == gameModel.getPlayerByNumber(y).getPlayingLetter()) {
        return;
      }
    }
    gameModel.addPlayer(new OXOPlayer(symbol));
    NumberOfPlayers = gameModel.getNumberOfPlayers();
  }
}
