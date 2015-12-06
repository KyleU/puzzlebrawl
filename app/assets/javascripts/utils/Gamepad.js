/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var Gamepad = function(game) {
    this.game = game;
  };

  Gamepad.prototype.init = function() {
    var g = this.game;
    g.input.gamepad.start();

    var pad = g.input.gamepad.pad1;

    function input(s) { return function() { g.onInput(s); }; }

    var buttonA = pad.getButton(Phaser.Gamepad.XBOX360_A);
    var buttonB = pad.getButton(Phaser.Gamepad.XBOX360_B);

    var buttonDPadLeft = pad.getButton(Phaser.Gamepad.XBOX360_DPAD_LEFT);
    var buttonDPadRight = pad.getButton(Phaser.Gamepad.XBOX360_DPAD_RIGHT);
    var buttonDPadUp = pad.getButton(Phaser.Gamepad.XBOX360_DPAD_UP);
    var buttonDPadDown = pad.getButton(Phaser.Gamepad.XBOX360_DPAD_DOWN);

    if(buttonA !== null) {
      buttonA.onDown.add(input('active-clockwise'));
      buttonB.onDown.add(input('active-counter-clockwise'));

      buttonDPadLeft.onDown.add(input('active-left'));
      buttonDPadRight.onDown.add(input('active-right'));
      buttonDPadUp.onDown.add(input('active-drop'));
      buttonDPadDown.onDown.add(input('active-step'));
    }
  };

  return Gamepad;
});
