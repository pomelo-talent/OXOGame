package edu.uob;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uob.OXOMoveException.*;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {
  OXOModel model;
  OXOController controller;

  // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
  // and O player
  private static OXOModel createStandardModel() {
    OXOModel model = new OXOModel(3, 3, 3);
    model.addPlayer(new OXOPlayer('X'));
    model.addPlayer(new OXOPlayer('O'));
    return model;
  }


  // we make a new board for every @Test (i.e. this method runs before every @Test test case)
  @BeforeEach
  void setup() {
    model = createStandardModel();
    controller = new OXOController(model);
  }


  // here's a basic test for the `controller.handleIncomingCommand` method
  @Test
  void testHandleIncomingCommand() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");

    // A move has been made for A1 (i.e. the [0,0] cell on the board), let's see if that cell is
    // indeed owned by the player
    assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
  }


  // here's a complete game where we find out if someone won
  // Win horizontally
  @Test
  void testBasicWinWithA1A2A3() throws OXOMoveException {
    // take note of whose gonna made the first move
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("b1");
    // Player: X
    controller.handleIncomingCommand("a2");
    // Player: O
    controller.handleIncomingCommand("b2");
    // Player: X
    controller.handleIncomingCommand("a3");

    // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }


  // here's a complete game where we find out if someone won
  // Win vertically
  /*     [[X,O, ],
          [X,O, ],
          [X, , ] */
  @Test
  void testWinVertically() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("a2");
    // Player: X
    controller.handleIncomingCommand("b1");
    // Player: O
    controller.handleIncomingCommand("b2");
    // Player: X
    controller.handleIncomingCommand("c1");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }

  // Win Left-diagonally
  /*     [[X,O, ],
          [O,X, ],
          [ , ,X] */
  @Test
  void testWinDiaL() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("a2");
    // Player: X
    controller.handleIncomingCommand("b2");
    // Player: O
    controller.handleIncomingCommand("b1");
    // Player: X
    controller.handleIncomingCommand("c3");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
  }


  // Win Right-diagonally
  /*     [[ ,O,X],
          [O,X, ],
          [X, , ] */
  @Test
  void testWinDiaR() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Player: X
    controller.handleIncomingCommand("a3");
    // Player: O
    controller.handleIncomingCommand("a2");
    // Player: X
    controller.handleIncomingCommand("b2");
    // Player: O
    controller.handleIncomingCommand("b1");
    // Player: X
    controller.handleIncomingCommand("c1");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }


  /* Here's a game ends in a draw,
  e.g. [[X,O,X],
        [X,X,O],
        [O,X,O]] */
  @Test
  void testDraw() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("a2");
    // Player: X
    controller.handleIncomingCommand("b1");
    // Player: O
    controller.handleIncomingCommand("c1");
    // Player: X
    controller.handleIncomingCommand("b2");
    // Player: O
    controller.handleIncomingCommand("b3");
    // Player: X
    controller.handleIncomingCommand("c2");
    // Player: O
    controller.handleIncomingCommand("c3");
    // Player: X
    controller.handleIncomingCommand("a3");
    assertTrue(model.isGameDrawn());

  }


  @Test
  void testDynamicSize() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addRow();
    controller.addRow();
    // a 5*3 OXO board (where three of the same symbol in a line wins)
    assertEquals(
            5,
            model.getNumberOfRows(),
            "The number of rows expected to be %s but wasn't".formatted(5)
    );

    controller.removeRow();
    // a 4*3 OXO board (where three of the same symbol in a line wins)
    assertEquals(
            4,
            model.getNumberOfRows(),
            "The number of rows expected to be %s but wasn't".formatted(4)
    );

    controller.addColumn();
    controller.addColumn();
    // a 4*5 OXO board (where three of the same symbol in a line wins)
    assertEquals(
            5,
            model.getNumberOfColumns(),
            "The number of rows expected to be %s but wasn't".formatted(5)
    );

    controller.removeColumn();
    // a 4*4 OXO board (where three of the same symbol in a line wins)
    assertEquals(
            4,
            model.getNumberOfColumns(),
            "The number of rows expected to be %s but wasn't".formatted(4)
    );

  }


  /* [[X,X,X,X],
      [O,O,O, ],
      [ , , , ]
      [ , , , ]] */
  @Test
  void testIncWinThresh() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.addRow();
    controller.addColumn();
    controller.increaseWinThreshold();
    // a 4*4 OXO board (where four of the same symbol in a line wins)
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("b1");
    // Player: X
    controller.handleIncomingCommand("a2");
    // Player: O
    controller.handleIncomingCommand("b2");
    // Player: X
    controller.handleIncomingCommand("a3");
    // A1, A2, A3 is not a win here, the game is still pending, no winner now
    assertTrue(model.getWinner()==null);
    // Player: O
    controller.handleIncomingCommand("b3");
    // Player: X
    controller.handleIncomingCommand("a4");
    // OK, so A1, A2, A3, A4 is a win and that last A4 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }


  /* [[X,X, ],
      [O, , ],
      [ , , ]] */
  @Test
  void testDecWinThresh() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.decreaseWinThreshold();
    // a 3*3 OXO board (where two of the same symbol in a line wins)
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("b1");
    // Player: X
    controller.handleIncomingCommand("a2");
    // A1, A2 is a win here, and that last A2 move is made by the first player (players
    // alternative between moves) let's make an assertion to see whether the first moving player is
    // the winner here
    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }


    /* [[X,O, , , ],
        [ ,X,O, , ],
        [ , ,X,O, ]
        [ , , ,X, ]] */
  @Test
  void test45SizeWinDiaL() throws OXOMoveException {
    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.decreaseWinThreshold();
    // a 3*3 OXO board (where two of the same symbol in a line wins)
    // Player: X
    controller.handleIncomingCommand("a1");
    // Player: O
    controller.handleIncomingCommand("a2");
    // Player: X
    controller.handleIncomingCommand("b2");
    // Player: O
    controller.handleIncomingCommand("b3");
    // Player: X
    controller.handleIncomingCommand("c3");
    // Player: O
    controller.handleIncomingCommand("c4");
    // Player: X
    controller.handleIncomingCommand("d4");

    assertEquals(
            firstMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));

  }


  @Test
  void testMoveException() throws OXOMoveException {
    assertThrows(OutsideCellRangeException.class, ()->controller.handleIncomingCommand(("a4")));
    assertThrows(OutsideCellRangeException.class, ()->controller.handleIncomingCommand(("d1")));

    assertThrows(InvalidIdentifierLengthException.class, ()->controller.handleIncomingCommand("aa1"));
    assertThrows(InvalidIdentifierLengthException.class, ()->controller.handleIncomingCommand("a13"));

    assertThrows(InvalidIdentifierCharacterException.class, ()->controller.handleIncomingCommand("1a"));
    assertThrows(InvalidIdentifierCharacterException.class, ()->controller.handleIncomingCommand("ad"));

    OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    controller.handleIncomingCommand("a1");
    assertThrows(CellAlreadyTakenException.class, ()->controller.handleIncomingCommand("a1"));
  }


  /* [[X,O,O],
      [O,X,X],
      [C,C,C]] */
  @Test
  void testAddAddPlayers() throws OXOMoveException {
    // add 1 additional player
    controller.addAddPlayer('C');
    assertEquals(3, model.getNumberOfPlayers());
    // Player: "X“, index: 0
    controller.handleIncomingCommand("a1");
    // Player: "O“, index: 1
    controller.handleIncomingCommand("b1");
    // Player: "C", index: 2
    OXOPlayer thirdMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
    assertEquals(model.getCurrentPlayerNumber(), 2);
    controller.handleIncomingCommand("c1");
    assertEquals(thirdMovingPlayer, controller.gameModel.getCellOwner(2, 0));
    // Player: "X"
    controller.handleIncomingCommand("b2");
    // Player: "O"
    controller.handleIncomingCommand("a2");
    // Player: "C"
    controller.handleIncomingCommand("c2");
    // Player: "X"
    controller.handleIncomingCommand("b3");
    // Player: "O"
    controller.handleIncomingCommand("a3");
    // Player: "C"
    controller.handleIncomingCommand("c3");
    assertEquals(
            thirdMovingPlayer,
            model.getWinner(),
            "Winner was expected to be %s but wasn't".formatted(thirdMovingPlayer.getPlayingLetter()));

  }
}
