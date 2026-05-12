require("dotenv").config({ path: require("path").resolve(__dirname, "../../.env") });

const bcrypt = require("bcryptjs");
const connectDatabase = require("../config/db");
const User = require("../models/User");
const Play = require("../models/Play");
const Cast = require("../models/Cast");
const Comment = require("../models/Comment");

const seed = async () => {
  try {
    await connectDatabase();

    const adminPassword = await bcrypt.hash(process.env.ADMIN_PASSWORD || "Admin@123", 10);

    await User.findOneAndUpdate(
      { email: (process.env.ADMIN_EMAIL || "admin@nammamela.com").toLowerCase() },
      {
        name: "Namma Mela Admin",
        email: (process.env.ADMIN_EMAIL || "admin@nammamela.com").toLowerCase(),
        password: adminPassword,
        role: "admin",
      },
      { upsert: true, new: true }
    );

    await Play.findOneAndUpdate(
      {},
      {
        title: "Veera Tamizhan",
        genre: "Historical Drama",
        duration: "2h 20m",
        description:
          "A lively stage production about courage, heritage, and community told through music, dance, and dramatic storytelling.",
        poster: "https://images.unsplash.com/photo-1503095396549-807759245b35?auto=format&fit=crop&w=900&q=80",
      },
      { upsert: true, new: true, sort: { createdAt: -1 } }
    );

    if ((await Cast.countDocuments()) === 0) {
      await Cast.insertMany([
        {
          name: "Arun Prakash",
          role: "Lead Hero",
          bio: "A theatre performer known for powerful historical roles.",
          image: "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=600&q=80",
        },
        {
          name: "Maya Devi",
          role: "Narrator",
          bio: "Brings warmth and poetic timing to every live performance.",
          image: "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=600&q=80",
        },
      ]);
    }

    if ((await Comment.countDocuments()) === 0) {
      await Comment.insertMany([
        { name: "Kiran", message: "Booked for tonight. Super excited!" },
        { name: "Latha", message: "The cast line-up looks amazing." },
      ]);
    }

    console.log("Seed completed successfully");
    process.exit(0);
  } catch (error) {
    console.error("Seed failed", error);
    process.exit(1);
  }
};

seed();
