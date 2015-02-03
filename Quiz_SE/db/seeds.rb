# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

User.create(username: "ali.mohamed", first_name: "Ali", last_name: "Mohamed", type: "Student", faculty: "FOE", university: "ASU", department: "CSE", year: "2016", email: "ali.mohamed@maradona.com", password: "1234567890")