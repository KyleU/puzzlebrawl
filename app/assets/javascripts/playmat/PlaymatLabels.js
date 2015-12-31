/* global define:false */
/* global Phaser:false */
define([], function () {
  'use strict';

  var scoreStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
  var selfStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };
  var friendStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#0f0' };
  var enemyStyle = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#f00' };

  var PlaymatLabels = function(playmat, playerId, name, score) {
    this.playmat = playmat;

    var style;
    if(playmat.self === playerId) {
      style = selfStyle;
    } else {
      var selfTeam = playmat.players[playmat.self].team;
      var targetTeam = playmat.players[playerId].team;
      if(selfTeam === targetTeam) {
        style = friendStyle;
      } else {
        style = enemyStyle;
      }
    }

    this.nameLabel = new Phaser.Text(playmat.game, 0, 0, name, style);
    this.nameLabel.name = 'name-label-' + name;
    playmat.add(this.nameLabel);

    this.scoreLabel = new Phaser.Text(playmat.game, 0, 0, score, scoreStyle);
    this.scoreLabel.name = 'score-label-' + name;
    this.scoreLabel.anchor.set(1, 0);
    playmat.add(this.scoreLabel);
  };

  PlaymatLabels.prototype.hide = function() {
    this.nameLabel.visible = false;
    this.scoreLabel.visible = false;
  };

  PlaymatLabels.prototype.resize = function(xOffset, width, yOffset) {
    this.nameLabel.x = xOffset;
    this.nameLabel.y = yOffset;

    this.scoreLabel.x = xOffset + width;
    this.scoreLabel.y = yOffset;
  };

  return PlaymatLabels;
});
