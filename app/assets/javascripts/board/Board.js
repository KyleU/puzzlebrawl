/* global define:false */
/* global Phaser:false */
define(['gem/Gem', 'board/BoardGems', 'board/BoardMutations'], function (Gem, BoardGems, BoardMutations) {
  'use strict';

  function Board(model, owner, game) {
    this.key = model.key;
    this.w = model.width;
    this.h = model.height;
    this.model = model;

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
          BoardGems.addGem(this, g, x, y);
        }
      }
    }
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

  Board.prototype.applyMutations = function(mutations) {
    BoardMutations.applyMutations(this, mutations);
  };

  return Board;
});
