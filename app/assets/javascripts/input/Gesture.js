/* global define:false */
/* global Phaser:false */
define(['input/GestureSignals'], function (GestureSignals) {
  'use strict';

  var Gesture = function(game) {
    this.game = game;
    this.signals = new GestureSignals(game);
  };

  Gesture.prototype.init = function() {
    var g = this.game;
    for(var addIdx = 0; addIdx < 4; addIdx++) {
      g.input.addPointer();
    }
    // Previously included [game.input.mousePointer]
    this.pointers = [ g.input.pointer1, g.input.pointer2, g.input.pointer3, g.input.pointer4, g.input.pointer5, g.input.pointer6];
    this.statuses = [];
    for(var pIdx = 0; pIdx < this.pointers.length; pIdx++) {
      this.statuses[pIdx] = {
        swipeDispatched: false,
        holdDispatched: false,
        isTouching: false,
        isHolding: false
      };
    }
  };

  Gesture.prototype.update = function() {
    for(var pIdx = 0; pIdx < this.pointers.length; pIdx++) {
      var pointer = this.pointers[pIdx];
      var status = this.statuses[pIdx];
      if(pointer.isDown) {
        var distance = Phaser.Point.distance(pointer.position, pointer.positionDown);
        var duration = pointer.duration;

        this.updateSwipe(pointer, status, distance, duration);
        this.updateTouch(pointer, status, distance, duration);
      } else {
        this.cancelSwipe(status);
        this.cancelTouch(pointer, status);
      }
    }
  };

  Gesture.TIMES = {
    HOLD: 250,
    SWIPE: 250
  };

  Gesture.prototype.cancelSwipe = function(status) {
    status.swipeDispatched = false;
  };

  Gesture.prototype.cancelTouch = function(pointer, status) {
    if (status.isTouching) {
      this.signals.onTap.dispatch(this, pointer);
    }

    status.isTouching = false;
    status.isHolding = false;
    status.holdDispatched = false;
  };

  Gesture.prototype.updateSwipe = function(pointer, status, distance, duration) {
    if (!status.swipeDispatched && distance > 150 &&  duration > 100 && duration < Gesture.TIMES.SWIPE) {
      this.signals.onSwipe.dispatch(this, pointer);
      status.swipeDispatched = true;
    }
  };

  Gesture.prototype.updateTouch = function(pointer, status, distance, duration) {
    if (distance < 10) {
      if (duration < Gesture.TIMES.HOLD) {
        status.isTouching = true;
      } else {
        status.isTouching = false;
        status.isHolding = true;

        if (!status.holdDispatched) {
          status.holdDispatched = true;
          this.signals.onHold.dispatch(this, pointer);
        }
      }
    } else {
      status.isTouching = false;
      status.isHolding = false;
    }
  };

  return Gesture;
});
