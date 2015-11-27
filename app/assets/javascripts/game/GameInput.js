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
      default:
        console.log('Unhandled input [' + t + '].');
        break;
    }
  };

  return GameInput;
});
