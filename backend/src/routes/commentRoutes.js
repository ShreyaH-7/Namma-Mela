const express = require("express");
const { getComments, addComment } = require("../controllers/commentController");
const { requireAuth } = require("../middleware/authMiddleware");

const router = express.Router();

router.get("/comments", requireAuth, getComments);
router.post("/comments", requireAuth, addComment);

module.exports = router;
