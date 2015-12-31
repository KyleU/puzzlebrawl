/* global define:false */
/* global _:false */
define(['utils/Config'], function (Config) {
  'use strict';

  //var targetAspectRatios = [
  //  9 / 16,
  //  3 / 4,
  //  4 / 3,
  //  16 / 9
  //];

  var marginPx = 32;

  var selfScale = 1;
  var othersScale = 0.8;

  var PlaymatLayout = function(playmat) {
    this.playmat = playmat;
  };

  PlaymatLayout.prototype.closestAspectRatio = function() {
    //var world = this.playmat.game.world;
    //var aspectRatio = world.width / world.height;
    //var closestRatio = Phaser.ArrayUtils.findClosest(aspectRatio, targetAspectRatios);
    //console.log(targetAspectRatios, aspectRatio, closestRatio);
  };

  PlaymatLayout.prototype.refreshLayout = function() {
    var p = this.playmat;

    this.closestAspectRatio();

    var selfId = this.playmat.self;
    if(selfId === null) {
      throw 'No self id.';
    }

    var originalSize = [p.w, p.h];

    var splitPlayers = _.partition(p.players, function(player) { return player.id === selfId; });

    if(splitPlayers.length !== 2 || splitPlayers[0].length !== 1) {
      throw 'Incomplete board definitions';
    }

    var self = splitPlayers[0][0];
    var others = splitPlayers[1];

    var xOffset = marginPx;

    self.labels.resize(xOffset, self.board.w * Config.tile.size * selfScale, marginPx + (self.board.h * Config.tile.size * selfScale));

    self.board.x = xOffset;
    self.board.y = marginPx;

    self.board.scale = { x: selfScale, y: selfScale };

    xOffset += self.board.width + (Config.tile.size / 4);

    _.each(others, function(player) {
      player.labels.resize(xOffset, player.board.w * Config.tile.size * othersScale, marginPx + (player.board.h * Config.tile.size * othersScale));

      player.board.x = xOffset;
      player.board.y = marginPx;

      player.board.scale = { x: othersScale, y: othersScale };

      xOffset += player.board.width + marginPx;
    });

    p.w = xOffset;
    p.h = (self.board.h + 1) * Config.tile.size;

    if(p.w !== originalSize[0] || p.h !== originalSize[1]) {
      this.playmat.resizer.resize();
    }
  };

  return PlaymatLayout;
});
