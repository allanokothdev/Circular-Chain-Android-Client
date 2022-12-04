/* eslint-disable require-jsdoc */
/* eslint-disable no-unused-vars */
/* eslint-disable max-len */

// Firebase
const functions = require("firebase-functions");
const admin = require("firebase-admin");
admin.initializeApp();

const ethers = require("ethers");
const maticMumbai = "https://rpc-mumbai.maticvigil.com";
const provider = new ethers.providers.JsonRpcProvider(maticMumbai);

const CircularChainABI = require("./abi/CircularChain.json");
const CircularChainContractAddress = "0x6523F56455647100d2F7eAAe00359e40E52dC1d3";


// CREATE NEW WALLET ADDRESS
exports.createWallet = functions.https.onCall(() => {
  const wallet = ethers.Wallet.createRandom();
  console.log(wallet.address);
  return {
    address: wallet.address,
    privateKey: wallet.privateKey,
  };
});

// FETCH SUPPLY CHAIN STAGES
exports.fetchStages = functions.https.onCall(async (_data, context) => {
  try {
    const privateKey = _data.key;
    const batchId = _data.batch;
    const signer = new ethers.Wallet(privateKey, provider);
    const circularContract = new ethers.Contract(CircularChainContractAddress, CircularChainABI, signer);
    // fetching the product stages using their batch ID from the Circular Chain contracts
    const data = await circularContract.fetchBatchStages(batchId);

    return data;
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


// FETCH SUPPLY CHAIN BATCH STAGES
exports.fetchBatchDetails = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const batchId = data.batch;
    const signer = new ethers.Wallet(privateKey, provider);
    const circularContract = new ethers.Contract(CircularChainContractAddress, CircularChainABI, signer);
    // fetching the respective batch details
    const data = await circularContract.fetchBatchDetails(batchId);

    return data;
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


// FETCH SUPPLY CHAIN STAGES
exports.calculateAggregateESGScore = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const batchId = data.batch;
    const signer = new ethers.Wallet(privateKey, provider);
    const circularContract = new ethers.Contract(CircularChainContractAddress, CircularChainABI, signer);
    // fetching the respective batch details
    const data = await circularContract.calculateAggregateESGScore(batchId);

    return data;
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});


// ADD NEW PRODUCT BATCH
exports.createBatch = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const brandId = data.brandId;
    const stakeholders = data.stakeholders;
    const signer = new ethers.Wallet(privateKey, provider);
    const circularContract = new ethers.Contract(CircularChainContractAddress, CircularChainABI, signer);
    // Create New Batch
    const transaction = await circularContract.createBatch(brandId, stakeholders);
    const tx = await transaction.wait();

    return "data";
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});

exports.addNewStage = functions.https.onCall(async (data, context) => {
  try {
    const privateKey = data.key;
    const address = data.address;
    const batchId = data.batch;
    const title = data.title;
    const summary = data.summary;
    const location = data.location;
    const natureScore = data.natureScore;
    const climateScore = data.climateScore;
    const labourScore = data.labourScore;
    const communityScore = data.communityScore;
    const wasteScore = data.wasteScore;
    const signer = new ethers.Wallet(privateKey, provider);
    const circularContract = new ethers.Contract(CircularChainContractAddress, CircularChainABI, signer);
    // Add New Supply Chain Stage
    const transaction = await circularContract.addNewStage(batchId, title, summary, location, [natureScore, climateScore, labourScore, communityScore, wasteScore]);
    const tx = await transaction.wait();

    return "Success";
  } catch (error) {
    console.error(error);
    // Re-throwing the error as an HttpsError so that the client gets the error details.
    throw new functions.https.HttpsError("unknown", error.message, error);
  }
});
