/* global define:false */
define(['state/GameState', 'gem/Gem', 'board/Board', 'playmat/Playmat'], function (GameState, Gem, Board, Playmat) {
  'use strict';

  function Sandbox(client) {
    GameState.call(this, 'sandbox', client);
  }

  Sandbox.prototype = Object.create(GameState.prototype);
  Sandbox.prototype.constructor = Sandbox;

  Sandbox.prototype.preload = function() {

  };

  Sandbox.prototype.create = function() {
    GameState.prototype.create.apply(this, arguments);

    this.playmat = new Playmat(this.client);

    var board = new Board('sandbox', 6, 12, this.client);
    board.x = 32;
    board.y = 32;

    this.playmat.add(board);

    var g1 = new Gem({
      id: 0,
      color: 'r'
    }, this.client);
    var g2 = new Gem({
      id: 1,
      color: 'b',
      crash: true
    }, this.client);
    var g3 = new Gem({
      id: 2,
      color: 'g',
      timer: 3
    }, this.client);

    board.addGem(g1, 0, 0);
    board.addGem(g2, 1, 0);
    board.addGem(g3, 2, 0);
  };

  Sandbox.prototype.resize = function() {
    if(this.playmat !== undefined) {
      this.playmat.resizer.resize();
    }
  };

  return Sandbox;
});
