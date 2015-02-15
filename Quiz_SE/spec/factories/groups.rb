FactoryGirl.define do
  factory :group do
  	title "CSE"
  	sequence(:group_name) { |i| "grouptest#{i}" }
  	subject "CSE"
  	year "2015"
    description "Join the group"
  end
end