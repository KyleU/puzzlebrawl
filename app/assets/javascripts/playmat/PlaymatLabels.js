/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var style = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };

  var PlaymatLabels = function(playmat, name, score) {
    this.playmat = playmat;

    this.nameLabel = new Phaser.Text(playmat.game, 0, 0, name, style);
    this.nameLabel.name = 'name-label-' + name;
    playmat.add(this.nameLabel);

    this.scoreLabel = new Phaser.Text(playmat.game, 0, 0, score, style);
    this.scoreLabel.name = 'score-label-' + name;
    this.scoreLabel.anchor.set(1, 0);
    playmat.add(this.scoreLabel);
  };

  PlaymatLabels.prototype.resize = function(xOffset, width, yOffset) {
    this.nameLabel.x = xOffset;
    this.nameLabel.y = yOffset;

    this.scoreLabel.x = xOffset + width;
    this.scoreLabel.y = yOffset;
  };

  return PlaymatLabels;
});
