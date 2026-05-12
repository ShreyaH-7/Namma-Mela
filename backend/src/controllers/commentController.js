const Comment = require("../models/Comment");

const getComments = async (_req, res) => {
  try {
    const comments = await Comment.find().sort({ createdAt: -1 });
    return res.json(comments);
  } catch (error) {
    return res.status(500).json({ message: "Unable to fetch comments", error: error.message });
  }
};

const addComment = async (req, res) => {
  try {
    const { name, message } = req.body;

    if (!name || !message) {
      return res.status(400).json({ message: "Name and message are required" });
    }

    const comment = await Comment.create({ name, message });
    return res.status(201).json(comment);
  } catch (error) {
    return res.status(500).json({ message: "Unable to add comment", error: error.message });
  }
};

module.exports = { getComments, addComment };
