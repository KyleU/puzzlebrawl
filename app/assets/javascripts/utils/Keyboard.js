/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  function toggleDebug() {
    var debugPanels = document.getElementsByClassName('pdebug');
    if(debugPanels.length === 1) {
      if(debugPanels[0].style.display === 'none' || debugPanels[0].style.display === '') {
        debugPanels[0].style.display = 'block';
      } else {
        debugPanels[0].style.display = 'none';
      }
    }
  }

  var Keyboard = function(client) {
    this.client = client;
  };

  Keyboard.prototype.init = function() {
    var c = this.client;

    var sandboxKey = c.input.keyboard.addKey(Phaser.Keyboard.X);
    sandboxKey.onDown.add(function() {
      c.sandbox();
    });

    var debugKey = c.input.keyboard.addKey(Phaser.Keyboard.SPACEBAR);
    debugKey.onDown.add(toggleDebug);
  };

  Keyboard.prototype.enable = function() {
    this.client.input.keyboard.enabled = true;
  };

  Keyboard.prototype.disable = function() {
    this.client.input.keyboard.enabled = false;
  };

  return Keyboard;
});
