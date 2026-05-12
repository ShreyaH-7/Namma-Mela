const express = require("express");
const { getSeats, createBooking, resetSeats } = require("../controllers/bookingController");
const { requireAuth, requireAdmin } = require("../middleware/authMiddleware");

const router = express.Router();

router.get("/seats", requireAuth, getSeats);
router.post("/booking", requireAuth, createBooking);
router.delete("/reset-seats", requireAuth, requireAdmin, resetSeats);

module.exports = router;
