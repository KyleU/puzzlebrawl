/* global define:false */
/* global Phaser:false */
define([
  'gem/Gem', 'board/BoardGems', 'board/BoardActiveGems', 'board/BoardMutations', 'board/BoardScore'
], function (Gem, BoardGems, BoardActiveGems, BoardMutations, BoardScore) {
  'use strict';

  function Board(model, game) {
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;
    this.model = model;

    this.activeGemLocations = [];

    this.gems = {};
    Phaser.Group.call(this, game, null, 'board-' + model.key);

    game.add.existing(this);
    this.bgTileSprite = new Phaser.TileSprite(game, 0, 0, 128 * this.w, 128 * this.h, 'board-bg-a');

    this.bgTileSprite.name = 'background';
    this.add(this.bgTileSprite);

    for(var y = 0; y < this.h; y++) {
      for(var x = 0; x < this.w; x++) {
        var g = model.spaces[x][y];
        if(g !== null) {
          this.addGem(g, x, y);
        }
      }
    }

    BoardScore.init(this, model.score);
  }

  Board.prototype = Phaser.Group.prototype;
  Board.prototype.constructor = Board;

  Board.prototype.changeScore = function(delta) {
    BoardScore.changeScore(this, delta);
  };

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
    console.log(x, y, v);
  };

  Board.prototype.dump = function() {
    var msg = '';
    for(var hIdx = this.h - 1; hIdx >= 0; hIdx--) {
      for(var wIdx = 0; wIdx < this.w; wIdx++) {
        if(this.at(wIdx, hIdx) === null) {
          msg += '.';
        } else {
          msg += 'O';
        }
      }
      msg += '\n';
    }
    console.log(msg);
  };

  Board.prototype.applyMutations = function(mutations) {
    BoardMutations.applyMutations(this, mutations);
  };

  Board.prototype.setActiveGems = function(ags) {
    BoardActiveGems.setActiveGems(this, ags);
  };

  Board.prototype.addGem = function(gem, x, y) {
    BoardGems.addGem(this, gem, x, y);
  };

  Board.prototype.changeGem = function(newGem, x, y) {
    BoardGems.changeGem(this, newGem, x, y);
  };

  Board.prototype.moveGem = function(x, y, xDelta, yDelta) {
    BoardGems.moveGem(this, x, y, xDelta, yDelta);
  };

  Board.prototype.removeGem = function(x, y) {
    BoardGems.removeGem(this, x, y);
  };

  return Board;
});
