class Group < ActiveRecord::Base
	has_many :memberships
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
end
