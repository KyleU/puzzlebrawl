/* global define:false */
/* global Phaser:false */
define(['input/GamepadSignals'], function (GamepadSignals) {
  'use strict';

  var Gamepad = function(game) {
    this.game = game;
    this.signals = new GamepadSignals(game);
  };

  Gamepad.prototype.init = function() {
    var g = this.game;
    g.input.gamepad.start();
    var pad = g.input.gamepad.pad1;
    this.pad1 = pad;

    this.status = {
      leftDispatched: false,
      rightDispatched: false,
      upDispatched: false,
      downDispatched: false
    };

    function addButtons() {
      function input(s) { return function() { g.onInput(s); }; }

      if(pad.connected) {
        pad.getButton(Phaser.Gamepad.XBOX360_A).onDown.add(input('active-clockwise'));
        pad.getButton(Phaser.Gamepad.XBOX360_B).onDown.add(input('active-counter-clockwise'));

        pad.getButton(Phaser.Gamepad.XBOX360_RIGHT_TRIGGER).onDown.add(input('active-clockwise'));
        pad.getButton(Phaser.Gamepad.XBOX360_LEFT_TRIGGER).onDown.add(input('active-counter-clockwise'));

        pad.getButton(Phaser.Gamepad.XBOX360_DPAD_LEFT).onDown.add(input('active-left'));
        pad.getButton(Phaser.Gamepad.XBOX360_DPAD_RIGHT).onDown.add(input('active-right'));
        pad.getButton(Phaser.Gamepad.XBOX360_DPAD_UP).onDown.add(input('active-step'));
        pad.getButton(Phaser.Gamepad.XBOX360_DPAD_DOWN).onDown.add(input('active-drop'));

        pad.getButton(Phaser.Gamepad.XBOX360_LEFT_BUMPER).onDown.add(input('target-previous'));
        pad.getButton(Phaser.Gamepad.XBOX360_RIGHT_BUMPER).onDown.add(input('target-next'));
      }
    }

    pad.addCallbacks(this, { onConnect: addButtons });
    addButtons();
  };

  Gamepad.prototype.update = function() {
    var x = this.pad1.axis(Phaser.Gamepad.XBOX360_STICK_LEFT_X);
    var y = this.pad1.axis(Phaser.Gamepad.XBOX360_STICK_LEFT_Y);

    if(x < -0.5) {
      if(!this.status.leftDispatched) {
        this.status.leftDispatched = true;
        this.signals.onAnalogLeft.dispatch();
      }
    } else {
      this.status.leftDispatched = false;
    }
    if(x > 0.5) {
      if(!this.status.rightDispatched) {
        this.status.rightDispatched = true;
        this.signals.onAnalogRight.dispatch();
      }
    } else {
      this.status.rightDispatched = false;
    }

    if(y < -0.5) {
      if(!this.status.upDispatched) {
        this.status.upDispatched = true;
        this.signals.onAnalogUp.dispatch();
      }
    } else {
      this.status.upDispatched = false;
    }
    if(y > 0.5) {
      if(!this.status.downDispatched) {
        this.status.downDispatched = true;
        this.signals.onAnalogDown.dispatch();
      }
    } else {
      this.status.downDispatched = false;
    }
  };

  return Gamepad;
});
