/* global define:false */
define(['state/GameState', 'gem/Gem', 'board/Board', 'playmat/Playmat', 'utils/Keyboard'], function (GameState, Gem, Board, Playmat, Keyboard) {
  'use strict';

  function Sandbox(game) {
    GameState.call(this, 'sandbox', game);
  }

  Sandbox.prototype = Object.create(GameState.prototype);
  Sandbox.prototype.constructor = Sandbox;

  Sandbox.prototype.preload = function() {

  };

  function addBigGem(board, game) {
    var g5 = new Gem({ id: 4, color: 'b', width: 3, height: 3, role: 'bl' }, game);
    board.addGem(g5, 3, 0);
    var g6 = new Gem({ id: 5, color: 'b', width: 3, height: 3, role: 'b' }, game);
    board.addGem(g6, 4, 0);
    var g7 = new Gem({ id: 6, color: 'b', width: 3, height: 3, role: 'br' }, game);
    board.addGem(g7, 5, 0);
    var g8 = new Gem({ id: 7, color: 'b', width: 3, height: 3, role: 'l' }, game);
    board.addGem(g8, 3, 1);
    var g9 = new Gem({ id: 8, color: 'b', width: 3, height: 3, role: 'c' }, game);
    board.addGem(g9, 4, 1);
    var g10 = new Gem({ id: 9, color: 'b', width: 3, height: 3, role: 'r' }, game);
    board.addGem(g10, 5, 1);
    var g11 = new Gem({ id: 10, color: 'b', width: 3, height: 3, role: 'ul' }, game);
    board.addGem(g11, 3, 2);
    var g12 = new Gem({ id: 11, color: 'b', width: 3, height: 3, role: 't' }, game);
    board.addGem(g12, 4, 2);
    var g13 = new Gem({ id: 12, color: 'b', width: 3, height: 3, role: 'ur' }, game);
    board.addGem(g13, 5, 2);
  }

  Sandbox.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.game.keyboard = new Keyboard(this.game);
    this.game.keyboard.init();

    this.game.send('StartBrawl', { scenario: 'sandbox' });

    this.playmat = new Playmat(this.game);

    var board = new Board('sandbox', 6, 12, this.game);
    board.x = 32;
    board.y = 32;

    this.playmat.add(board);

    var g1 = new Gem({ id: 0, color: 'r' }, this.game);
    board.addGem(g1, 0, 0);

    var g2 = new Gem({ id: 1, color: 'g', timer: 3 }, this.game);
    board.addGem(g2, 1, 0);

    var g3 = new Gem({ id: 2, color: 'y' }, this.game);
    board.addGem(g3, 2, 0);
    var g4 = new Gem({ id: 3, color: 'y', crash: true }, this.game);
    board.addGem(g4, 2, 1);

    addBigGem(board, this.game);
  };

  Sandbox.prototype.resize = function() {
    if(this.playmat !== undefined) {
      this.playmat.resizer.resize();
    }
  };

  return Sandbox;
});
