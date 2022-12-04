// SPDX-License-Identifier: UNLICENSED
pragma solidity 0.8.9;

import "hardhat/console.sol";
import "@openzeppelin/contracts/utils/Counters.sol";
import "@openzeppelin/contracts/security/ReentrancyGuard.sol";

contract CircularChain is ReentrancyGuard {

    using Counters for Counters.Counter;
    Counters.Counter private batchIds;
    Counters.Counter private stageIds;

    mapping (uint256 => Batch) public batchList;
    mapping (uint256 => Stage) public stageList;

    address payable public admin;

    struct Esg {
        uint32 natureScore;
        uint32 climateScore;
        uint32 labourScore;
        uint32 communityScore;
        uint32 wasteScore;
    }

    struct ESGScoresNLength {
        Esg esg;
        uint256 noOfItems;
    }

    struct Batch {
        uint256 batchId;
        address publisher;
        address[] stakeholders;
        uint256 brandId;
    }

    struct Stage {
        uint256 stageId;
        string title;
        string summary;
        address publisher;
        uint256 timestamp;
        string location;
        Esg esgScore;
        uint256 batchId;
    }

    event BatchItemCreated (
        uint256 indexed batchId,
        address publisher,
        address[] stakeholders,
        uint256 brandId
    );

    event StageItemCreated (
      uint256 indexed stageId,
      string title,
      string summary,
      address publisher,
      uint256 timestamp,
      string location,
      Esg esgScore
    );

    constructor() payable {
        admin = payable(msg.sender);
    }

    function createBatch(uint256 _brandId, address[] memory _stakeholders)
    public nonReentrant {
        //require(msg.value > 0, "Price must be at least 1 wei");
        //require(msg.value == listingPrice, "Price must be equal to listing price");

        batchIds.increment();
        uint256 newProductId = batchIds.current();

        batchList[newProductId] = Batch(newProductId, msg.sender, _stakeholders, _brandId);

        emit BatchItemCreated(newProductId, msg.sender, _stakeholders, _brandId);
    }


    function fetchBatchDetails(uint256 _batchId) public view returns (address, address[] memory, uint256, Esg memory, uint256) {
        Batch memory product = batchList[_batchId];
        ESGScoresNLength memory esgScoresNLength = calculateAggregateESGScore(_batchId);
        return (product.publisher, product.stakeholders, product.brandId, esgScoresNLength.esg, esgScoresNLength.noOfItems);
    }

    function addNewStage(uint256 _batchId, string memory _title, string memory _summary, string memory _location,
    Esg memory _esg) public {
        address[] memory _stakeholders = batchList[_batchId].stakeholders;

        for(uint i=0; i<_stakeholders.length; i++){
            if (msg.sender == _stakeholders[i]) {

                stageIds.increment();
                uint256 newStageId = stageIds.current();

                stageList[newStageId] = Stage(newStageId, _title, _summary, msg.sender, block.timestamp,
                _location, _esg, _batchId);
                
                emit StageItemCreated(newStageId, _title, _summary, msg.sender, block.timestamp, _location, _esg);
                
            } 
        }
    }

    function fetchBatchStages(uint256 _batchId) public view returns (Stage[] memory) {
        uint totalItemCount = stageIds.current();
        uint itemCount = 0;
        uint currentIndex = 0;

        for (uint i = 0; i < totalItemCount; i++) {
            if (stageList[i + 1].batchId == _batchId) {
            itemCount += 1;
        }
      }

      Stage[] memory items = new Stage[](itemCount);
      for (uint i = 0; i < totalItemCount; i++) {
        if (stageList[i + 1].batchId == _batchId) {
          uint currentId = i + 1;
          Stage storage currentItem = stageList[currentId];
          items[currentIndex] = currentItem;
          currentIndex += 1;
        }
      }
      return items;
    }

    function calculateAggregateESGScore(uint256 _batchId) public view returns (ESGScoresNLength memory) {
        uint totalItemCount = stageIds.current();
        uint itemCount = 0;
        uint currentIndex = 0;

        uint32 totNatureScore = 0;
        uint32 totClimateScore = 0;
        uint32 totLabourScore = 0;
        uint32 totCommunityScore = 0;
        uint32 totWasteScore = 0;

        for (uint i = 0; i < totalItemCount; i++) {
            if (stageList[i + 1].batchId == _batchId) {
            itemCount += 1;
        }
      }

      Stage[] memory items = new Stage[](itemCount);
      for (uint i = 0; i < totalItemCount; i++) {
        if (stageList[i + 1].batchId == _batchId) {
          uint currentId = i + 1;
          Stage storage currentItem = stageList[currentId];

          Esg memory esgItem = currentItem.esgScore;

          totNatureScore += esgItem.natureScore;
          totClimateScore += esgItem.climateScore;
          totLabourScore += esgItem.labourScore;
          totCommunityScore += esgItem.communityScore;
          totWasteScore += esgItem.wasteScore;

          items[currentIndex] = currentItem;
          currentIndex += 1;
        }
      }

      Esg memory esgScores = Esg(totNatureScore, totClimateScore, totLabourScore, totCommunityScore, totWasteScore);

      ESGScoresNLength memory esgScoresNLength = ESGScoresNLength(esgScores, items.length);

      return (esgScoresNLength);
    }

    modifier onlyAdmin() {
        assert(msg.sender == admin);
        _;
    }
}


