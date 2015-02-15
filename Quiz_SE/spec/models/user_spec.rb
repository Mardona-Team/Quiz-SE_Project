require 'rails_helper'

RSpec.describe User, :type => :model do

  before(:each) do
    @user = create :user
  end

  context 'attributes' do
    it "has username" do
      @user.username = nil
      expect(@user).not_to be_valid
    end

    it "has email" do
      @user.email = nil
      expect(@user).not_to be_valid
    end

    it "has first name" do
      @user.first_name = nil
      expect(@user).not_to be_valid
    end

    it "has last name" do
      @user.last_name = nil
      expect(@user).not_to be_valid
    end

    it "has type" do
      @user.type = nil
      expect(@user).not_to be_valid
    end

    it "has unique username" do
      another_user = create :user
      @user.username = another_user.username
      expect(@user).not_to be_valid
    end

    it "has valid username length" do
      @user.username = "foo"
      expect(@user).not_to be_valid
    end
  end

  context 'methods' do
    it "returns full name" do
      @user.first_name = "Foo"
      @user.last_name = "Bar"
      expect(@user.full_name).to eq("Foo Bar")
    end
  end
end
