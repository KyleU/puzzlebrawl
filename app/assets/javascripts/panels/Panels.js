/* global define:false */
define([], function () {
  'use strict';

  function Panels() {
    this.splash = document.getElementById('splash-panel');
    this.connecting = document.getElementById('connecting-panel');
    this.menu = document.getElementById('menu-panel');
    this.tests = document.getElementById('test-panel');
    this.scenarios = document.getElementById('scenario-panel');
    this.instructions = document.getElementById('instructions-panel');
    this.options = document.getElementById('options-panel');
    this.status = document.getElementById('status-panel');
    this.feedback = document.getElementById('feedback-panel');
    this.gameplay = document.getElementById('game-container');

    this.active = this.splash;
  }

  Panels.prototype.show = function(key) {
    var original = this.active;
    switch(key) {
      case 'connecting':
        this.active = this.connecting;
        break;
      case 'menu':
        this.active = this.menu;
        break;
      case 'instructions':
        this.active = this.instructions;
        break;
      case 'scenario':
        this.active = this.scenarios;
        break;
      case 'test':
        this.active = this.tests;
        break;
      case 'options':
        this.active = this.options;
        break;
      case 'status':
        this.active = this.status;
        break;
      case 'feedback':
        this.active = this.feedback;
        break;
      case 'gameplay':
        this.active = this.gameplay;
        break;
      default:
        throw 'Invalid key [' + key + '].';
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
