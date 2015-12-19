/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var initialized = false;
  var active = false;

  var headerStyle = { font: '120px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
  var bodyStyle = { font: '120px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };

  var group = null;
  var headerText = null;
  var bodyText = null;

  function init(game) {
    if(initialized) {
      throw 'Modal already initialized.';
    }
    group = new Phaser.Group(game, null, 'modal-dialog');

    headerText = new Phaser.Text(game, 0, 0, 'Test Header', headerStyle);
    group.add(headerText);

    bodyText = new Phaser.Text(game, 0, 0, 'Test Body', bodyStyle);
    group.add(bodyText);

    game.add.existing(group);

    initialized = true;
  }

  function show(s) {
    if(active) {
      throw 'Modal already active.';
    }
    active = true;
  }

  return {
    init: init,
    show: show
  };
});
