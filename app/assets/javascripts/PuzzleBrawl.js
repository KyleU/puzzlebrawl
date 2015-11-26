/* global define:false */
define(['game/Game'], function (Game) {
  'use strict';

  function PuzzleBrawl() {
    this.start();
  }

  PuzzleBrawl.prototype.start = function() {
    this.game = new Game();
  };

  return PuzzleBrawl;
});
