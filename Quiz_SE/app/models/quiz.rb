class Quiz < ActiveRecord::Base
	
	#Relatioships
	belongs_to :instructor, class_name: 'User', foreign_key: 'instructor_id'
	belongs_to :group
	has_many :questions
	accepts_nested_attributes_for :questions

	#Validations
	validates_presence_of :title
	validates_presence_of :subject
	validates_presence_of :year
	validates :marks, presence: true, :numericality => { less_than_or_equal_to: 10000, greater_than: 0 }

	before_save :set_published

    def set_published
    	self.status = true
    end

	def published
		is_published?
	end

	def is_published?
		if group_id
			"1"
		else
			"0"
		end
	end

	def show_full_details(options={})
		as_json(:only => [:id, :title, :subject, :year, :description, :marks, :created_at],
					:include => {
						:questions => {:only => [:id, :title],
							:methods => [:shuffled_answers]
						},
					})
	end
end
