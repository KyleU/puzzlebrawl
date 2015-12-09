/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var Gesture = function(game) {
    this.game = game;

    this.onSwipe = new Phaser.Signal();
    this.onTap = new Phaser.Signal();
    this.onHold = new Phaser.Signal();
  };

  Gesture.prototype.init = function() {
    var g = this.game;
    g.input.addPointer();
    g.input.addPointer();
    g.input.addPointer();
    g.input.addPointer();

    this.pointers = [
      g.input.mousePointer,
      g.input.pointer1,
      g.input.pointer2,
      g.input.pointer3,
      g.input.pointer4,
      g.input.pointer5,
      g.input.pointer6
    ];

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
    HOLD: 150,
    SWIPE: 250
  };

  Gesture.prototype.cancelSwipe = function(status) {
    status.swipeDispatched = false;
  };

  Gesture.prototype.cancelTouch = function(pointer, status) {
    if (status.isTouching) {
      console.log('Tap!', pointer);
      this.onTap.dispatch(this, pointer.positionDown);
    }

    status.isTouching = false;
    status.isHolding = false;
    status.holdDispatched = false;
  };

  Gesture.prototype.updateSwipe = function(pointer, status, distance, duration) {
    if (!status.swipeDispatched && distance > 150 &&  duration > 100 && duration < Gesture.TIMES.SWIPE) {
      console.log('Swipe!', pointer);
      this.onSwipe.dispatch(this, pointer.positionDown);
      status.swipeDispatched = true;
    }
  };

  Gesture.prototype.updateTouch = function(pointer, status, distance, duration) {
    var positionDown = pointer.positionDown;

    if (distance < 10) {
      if (duration < Gesture.TIMES.HOLD) {
        status.isTouching = true;
      } else {
        status.isTouching = false;
        status.isHolding = true;

        if (!status.holdDispatched) {
          status.holdDispatched = true;

          console.log('Hold!', pointer);
          this.onHold.dispatch(this, positionDown);
        }
      }
    } else {
      status.isTouching = false;
      status.isHolding = false;
    }
  };

  return Gesture;
});
