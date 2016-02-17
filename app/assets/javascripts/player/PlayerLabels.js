/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var scoreStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
  var selfStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
  var friendStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#0f0' };
  var enemyStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#f00' };

  var PlayerLabels = function(playerGroup, score) {
    this.playerGroup = playerGroup;

    var style;
    if(playerGroup.role === 'self') {
      style = selfStyle;
    } else {
      if(playerGroup.role === 'friend') {
        style = friendStyle;
      } else if(playerGroup.role === 'enemy') {
        style = enemyStyle;
      } else {
        throw new Error('Unknown role [' + playerGroup.role + '].');
      }
    }

    this.nameLabel = new Phaser.Text(playerGroup.game, 0, playerGroup.board.height, playerGroup.player.name, style);
    this.nameLabel.name = 'name-label-' + playerGroup.player.name;
    playerGroup.add(this.nameLabel);

    this.scoreLabel = new Phaser.Text(playerGroup.game, playerGroup.board.width, playerGroup.board.height, score, scoreStyle);
    this.scoreLabel.name = 'score-label-' + playerGroup.player.name;
    this.scoreLabel.anchor.set(1, 0);
    playerGroup.add(this.scoreLabel);
  };

  PlayerLabels.prototype.hide = function() {
    this.nameLabel.visible = false;
    this.scoreLabel.visible = false;
  };

  PlayerLabels.prototype.resize = function(xOffset, width, yOffset) {
    this.nameLabel.x = xOffset;
    this.nameLabel.y = yOffset;

    this.scoreLabel.x = xOffset + width;
    this.scoreLabel.y = yOffset;
  };

  PlayerLabels.prototype.destroy = function() {
    this.playerGroup.remove(this.nameLabel);
    this.nameLabel.destroy();
    this.playerGroup.remove(this.scoreLabel);
    this.scoreLabel.destroy();
  };

  return PlayerLabels;
});
