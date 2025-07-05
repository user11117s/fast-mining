## About

A Spigot plugin for 1.21 servers that gives you a smaller version of the Craftmine update.
Use `/createmine` to create and join a mine. Then use `/leavemine` to leave and destroy the mine.
You get a limited amount of time to spend in each mine. Once time runs out, you automatically leave and destroy the mine.
Mine **iron**, **gold** and **diamonds** so you can use them as currency to buy upgrades in the hub.
These upgrades enhance your mining experience.

## Dependencies
You must have MongoDB running on localhost:27017. It must have a "spigot" database with a "users" collection.
You can choose the URI, database name and collection name in the config.yml inside of plugins/FastMining (You must run the plugin once first to generate the file).
