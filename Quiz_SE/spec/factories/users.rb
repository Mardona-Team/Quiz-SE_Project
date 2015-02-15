FactoryGirl.define do
  factory :user do
    sequence(:email) { |i| "tester#{i}@example.com" }
    sequence(:username) { |i| "tester#{i}" }
    first_name "New"
    last_name "User"
    type "Instructor"
    password "SectretPassword"
  end
end
