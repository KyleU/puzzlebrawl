/* global define:false */
define([], function () {
  'use strict';

  function Panels() {
    this.splash = document.getElementById('splash-panel');
    this.connecting = document.getElementById('connecting-panel');
    this.menu = document.getElementById('menu-panel');
    this.instructions = document.getElementById('menu-panel');
    this.options = document.getElementById('menu-panel');
    this.gameplay = document.getElementById('game-container');

    this.active = this.splash;
  }

  Panels.prototype.show = function(key) {
    this.active.classList.remove('on');
    this.active.classList.add('off');
    console.log('Showing panel [' + key + '].');

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
      case 'options':
        this.active = this.options;
        break;
      case 'gameplay':
        this.active = this.gameplay;
        break;
      default:
        throw 'Invalid key [' + key + '].';
    }

    this.active.classList.remove('off');
    this.active.classList.add('on');
  };

  return Panels;
});
