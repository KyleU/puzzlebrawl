/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var GestureSignals = function(game) {
    this.game = game;
    this.onSwipe = new Phaser.Signal();
    this.onTap = new Phaser.Signal();
    this.onHold = new Phaser.Signal();

    this.onSwipe.add(this.swipeCallback, this, 0);
    this.onTap.add(this.tapCallback, this, 0);
    this.onHold.add(this.holdCallback, this, 0);
  };

  GestureSignals.prototype.swipeCallback = function(gestures, pointer) {
    if(this.game.playmat !== undefined) {
      this.game.playmat.input.onSwipe(pointer);
    }
  };

  GestureSignals.prototype.tapCallback = function(gestures, pointer) {
    if(this.game.playmat !== undefined) {
      this.game.playmat.input.onTap(pointer);
    }
  };

  GestureSignals.prototype.holdCallback = function(gestures, pointer) {
    if(this.game.playmat !== undefined) {
      this.game.playmat.input.onHold(pointer);
    }
  };

  return GestureSignals;
});
