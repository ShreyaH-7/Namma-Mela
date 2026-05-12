const Play = require("../models/Play");

const getPlay = async (_req, res) => {
  try {
    const play = await Play.findOne().sort({ updatedAt: -1 });

    if (!play) {
      return res.status(404).json({ message: "Play details not found" });
    }

    return res.json(play);
  } catch (error) {
    return res.status(500).json({ message: "Unable to fetch play", error: error.message });
  }
};

const savePlay = async (req, res) => {
  try {
    const { title, genre, duration, description, poster } = req.body;

    if (!title || !genre || !duration || !description || !poster) {
      return res.status(400).json({ message: "All play fields are required" });
    }

    const existingPlay = await Play.findOne();
    const payload = { title, genre, duration, description, poster };

    const play = existingPlay
      ? await Play.findByIdAndUpdate(existingPlay._id, payload, { new: true })
      : await Play.create(payload);

    return res.status(existingPlay ? 200 : 201).json(play);
  } catch (error) {
    return res.status(500).json({ message: "Unable to save play", error: error.message });
  }
};

module.exports = { getPlay, savePlay };
