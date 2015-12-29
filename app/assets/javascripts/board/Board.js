/* global define:false */
/* global Phaser:false */
define(['gem/Gem', 'board/BoardGems', 'board/BoardMutations', 'utils/Config', 'utils/Formatter'], function (Gem, BoardGems, BoardMutations, Config, Formatter) {
  'use strict';

  var style = { font: '64px Helvetica Neue, Helvetica, Arial, sans-serif', fill: '#fff' };

  function Board(owner, model, playerName, score, playmat) {
    this.owner = owner;
    this.playerName = playerName;
    this.score = score;
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;
    this.model = model;
    this.playmat = playmat;

    this.gems = {};
    Phaser.Group.call(this, playmat.game, null, 'board-' + model.key);

    this.game.add.existing(this);
    this.bgTileSprite = new Phaser.TileSprite(this.game, 0, 0, Config.tile.size * this.w, Config.tile.size * this.h, 'gems', 78);

    this.bgTileSprite.name = 'background';
    this.add(this.bgTileSprite);

    for(var y = 0; y < this.h; y++) {
      for(var x = 0; x < this.w; x++) {
        var g = model.spaces[x][y];
        if(g !== null && this.gems[g.id] === undefined) {
          BoardGems.addGem(this, g, x, y);
        }
      }
    }

    this.nameLabel = new Phaser.Text(playmat.game, 0, this.h * Config.tile.size, this.playerName, style);
    this.nameLabel.name = 'name-label-' + this.playerName;
    this.add(this.nameLabel);

    this.scoreLabel = new Phaser.Text(playmat.game, this.w * Config.tile.size, this.h * Config.tile.size, score, style);
    this.scoreLabel.name = 'score-label-' + this.playerName;
    this.scoreLabel.anchor.set(1, 0);
    this.add(this.scoreLabel);
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.at = function(x, y) {
    return this.model.spaces[x][y];
  };

  Board.prototype.clear = function(x, y, width, height) {
    var h = height;
    if(height === undefined) { h = 1; }
    var w = width;
    if(width === undefined) { w = 1; }
    for(var hIdx = 0; hIdx < h; hIdx++) {
      for(var wIdx = 0; wIdx < w; wIdx++) {
        this.model.spaces[x + wIdx][y + hIdx] = null;
      }
    }
  };

  Board.prototype.set = function(x, y, v) {
    if(v.height === undefined) { v.height = 1; }
    if(v.width === undefined) { v.width = 1; }

    for(var hIdx = 0; hIdx < v.height; hIdx++) {
      for(var wIdx = 0; wIdx < v.width; wIdx++) {
        this.model.spaces[x + wIdx][y + hIdx] = v;
      }
    }
  };

  Board.prototype.applyMutations = function(segments) {
    BoardMutations.applyMutations(this, segments, 0);
  };

  Board.prototype.addScore = function(delta) {
    this.score += delta;
    this.scoreLabel.text = Formatter.withCommas(this.score);
  };

  return Board;
});
