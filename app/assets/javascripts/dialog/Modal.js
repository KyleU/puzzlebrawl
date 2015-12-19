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

    headerText = new Phaser.Text(game, 0, 0, 'Modal Header', headerStyle);
    headerText.name = 'modal-header-text';
    headerText.visible = false;
    group.add(headerText);

    bodyText = new Phaser.Text(game, 0, 0, 'Modal Body', bodyStyle);
    bodyText.name = 'modal-body-text';
    bodyText.visible = false;
    group.add(bodyText);

    game.add.existing(group);

    initialized = true;
  }

  function show(s) {
    if(active) {
      throw 'Modal already active.';
    }
    headerText.visible = true;
    bodyText.visible = true;
    active = true;
  }

  function hide() {
    if(!active) {
      throw 'Modal not active.';
    }
    headerText.visible = false;
    bodyText.visible = false;
    active = false;
  }

  return {
    init: init,
    show: show,
    hide: hide
  };
});
