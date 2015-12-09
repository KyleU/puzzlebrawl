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
  };

  GestureSignals.prototype.swipeCallback = function(gestures, pointer) {
    var xDiff = pointer.position.x - pointer.positionDown.x;
    var yDiff = pointer.position.y - pointer.positionDown.y;

    if(Math.abs(xDiff) > (Math.abs(yDiff) * 2)) {
      if(xDiff > 0) {
        this.game.onInput('active-right');
      } else {
        this.game.onInput('active-left');
      }
    } else if(Math.abs(yDiff) > (Math.abs(xDiff) * 2)) {
      if(yDiff > 0) {
        this.game.onInput('active-step');
      } else {
        this.game.onInput('active-drop');
      }
    }

    console.log(pointer, xDiff, yDiff);
  };

  return GestureSignals;
});
