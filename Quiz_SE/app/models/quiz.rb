class Quiz < ActiveRecord::Base
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
	belongs_to :group
	has_many :questions
end
