class Group < ActiveRecord::Base

	NAME_REGEX = /[a-zA-Z](([\._\-][a-zA-Z0-9])|[a-zA-Z0-9])*[a-z0-9]/

	#Relationships
	has_many :memberships
	has_many :students, class_name: 'User', through: :memberships
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
	has_many :quizzes, through: :publications
	has_many :publications

	#Validations
	validates_presence_of :year
	validates_presence_of :subject
	validates :group_name, presence: true, uniqueness: true, format: NAME_REGEX, length: { minimum: 4, maximum: 12}
	validates :instructor, presence: true
end
