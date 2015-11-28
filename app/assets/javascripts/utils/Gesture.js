/* global define:false */
define([], function () {
  'use strict';

  var Gesture = function(game) {
    this.game = game;
  };

  Gesture.prototype.init = function() {
    var g = this.game;
    g.input.addPointer();
    g.input.addPointer();
    g.input.addPointer();
    g.input.addPointer();
  };

  return Gesture;
});
