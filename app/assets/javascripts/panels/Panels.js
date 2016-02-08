/* global define:false */
define([], function () {
  'use strict';

  function Panels() {
    this.elements = {
      'splash': document.getElementById('splash-panel'),
      'connecting': document.getElementById('connecting-panel'),
      'name': document.getElementById('name-panel'),
      'menu': document.getElementById('menu-panel'),
      'test': document.getElementById('test-panel'),
      'scenario': document.getElementById('scenario-panel'),
      'instructions': document.getElementById('instructions-panel'),
      'options': document.getElementById('options-panel'),
      'status': document.getElementById('status-panel'),
      'profile': document.getElementById('profile-panel'),
      'feedback': document.getElementById('feedback-panel'),
      'matchmaking': document.getElementById('matchmaking-panel'),
      'gameplay': document.getElementById('game-container')
    };

    this.active = this.elements.splash;
  }

  Panels.prototype.show = function(key) {
    var original = this.active;

    var panel = this.elements[key];
    if(panel === null || panel === undefined) {
      throw new Error('Invalid panel key [' + key + '].');
    } else {
      this.active = panel;
    }

    if(this.active !== original) {
      if(original !== null) {
        original.classList.remove('on');
        original.classList.add('off');
      }

      this.active.classList.remove('off');
      this.active.classList.add('on');
    }
  };

  return Panels;
});
