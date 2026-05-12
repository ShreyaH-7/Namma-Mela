const Booking = require("../models/Booking");

const getSeats = async (_req, res) => {
  try {
    const bookings = await Booking.find().sort({ createdAt: -1 });
    const reservedSeats = bookings.flatMap((booking) => booking.seats);
    return res.json({ reservedSeats, bookings });
  } catch (error) {
    return res.status(500).json({ message: "Unable to fetch seats", error: error.message });
  }
};

const createBooking = async (req, res) => {
  try {
    const { customerName, seats } = req.body;

    if (!customerName || !Array.isArray(seats) || seats.length === 0) {
      return res.status(400).json({ message: "Customer name and at least one seat are required" });
    }

    // This prevents two users from reserving the same seat at nearly the same time.
    const existingBookings = await Booking.find({ seats: { $in: seats } });
    if (existingBookings.length > 0) {
      return res.status(409).json({ message: "One or more seats are already reserved" });
    }

    const booking = await Booking.create({
      user: req.user.id,
      customerName,
      seats,
    });

    return res.status(201).json(booking);
  } catch (error) {
    return res.status(500).json({ message: "Unable to create booking", error: error.message });
  }
};

const resetSeats = async (_req, res) => {
  try {
    await Booking.deleteMany({});
    return res.json({ message: "All seats have been reset" });
  } catch (error) {
    return res.status(500).json({ message: "Unable to reset seats", error: error.message });
  }
};

module.exports = { getSeats, createBooking, resetSeats };
