/* global define:false */
define([], function () {
  'use strict';

  function GameInput(game) {
    this.game = game;
  }

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

  GameInput.prototype.onInput = function(t) {
    switch(t) {
      case 'toggle-debug':
        toggleDebug();
        break;
      case 'active-left':
        this.game.send('ActiveGemsLeft', {});
        break;
      case 'active-right':
        this.game.send('ActiveGemsRight', {});
        break;
      case 'active-clockwise':
        this.game.send('ActiveGemsClockwise', {});
        break;
      case 'active-counter-clockwise':
        this.game.send('ActiveGemsCounterClockwise', {});
        break;
      case 'active-step':
        this.game.send('ActiveGemsStep', {});
        break;
      default:
        console.log('Unhandled input [' + t + '].');
        break;
    }
  };

  return GameInput;
});