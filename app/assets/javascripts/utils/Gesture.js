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

  //Gesture.prototype.renderDebug = function() {
  //  var g = this.game;
  //  g.debug.pointer(g.input.mousePointer);
  //  g.debug.pointer(g.input.pointer1);
  //  g.debug.pointer(g.input.pointer2);
  //  g.debug.pointer(g.input.pointer3);
  //  g.debug.pointer(g.input.pointer4);
  //  g.debug.pointer(g.input.pointer5);
  //  g.debug.pointer(g.input.pointer6);
  //};

  return Gesture;
});
